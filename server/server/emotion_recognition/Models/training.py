import os

from keras.src.applications.inception_resnet_v2 import InceptionResNetV2
from keras.src.applications.inception_v3 import InceptionV3
from keras.src.applications.resnet import ResNet50, ResNet101
from keras.src.applications.resnet_v2 import ResNet50V2
from keras.src.applications.vgg16 import VGG16
from keras.src.applications.xception import Xception
from matplotlib import pyplot as plt
from tensorflow import keras

from common_functions import (
    create_model,
    get_data,
    fit,
    evaluation_model,
    saveModel,
)

if __name__ == "__main__":
    parameters = {
        "shape": [80, 80],
        "nbr_classes": 7,
        "train_path": "../../databases/FER-2013/train/",
        "test_path": "../../databases/FER-2013/test/",
        "batch_size": 8,
        "epochs": 50,
        "number_of_last_layers_trainable": 10,
        "learning_rate": 0.001,
        "nesterov": True,
        "momentum": 0.9,
    }
    model, filename, preprocess_input = None, None, None

    choice = input(
        "which models do you want to train?"
        "\n\t-1- resnet50"
        "\n\t-2- vgg16"
        "\n\t-3- xception"
        "\n\t-4- inception_resnet_v2"
        "\n\t-5- inception_v3"
        "\n\t-6- resnet50v2"
        "\n\t-7- resnet101"
        "\n>>>"
    )
    if choice == "1":
        model = ResNet50
        preprocess_input = keras.applications.resnet.preprocess_input
        filename = "resnet50"
    elif choice == "2":
        model = VGG16
        filename = "vgg16"
        preprocess_input = keras.applications.vgg16.preprocess_input
    elif choice == "3":
        model = Xception
        filename = "xception"
        preprocess_input = keras.applications.xception.preprocess_input
    elif choice == "4":
        model = InceptionResNetV2
        filename = "inception_resnet_v2"
        preprocess_input = keras.applications.inception_resnet_v2.preprocess_input
    elif choice == "5":
        model = InceptionV3
        filename = "inception_v3"
        preprocess_input = keras.applications.inception_v3.preprocess_input
    elif choice == "6":
        model = ResNet50V2
        filename = "resnet50v2"
        preprocess_input = keras.applications.resnet_v2.preprocess_input
    elif choice == "7":
        model = ResNet101
        filename = "resnet101"
        preprocess_input = keras.applications.resnet.preprocess_input
    else:
        print("you have to choose a number between 1 and 7")
        exit(1)

    if model is not None and filename is not None:
        print(
            f"{parameters}above parameters for the train, if you want another parameters, you can change them "
            f"directly in training.py "
        )

        train_files, test_files, train_generator, test_generator = get_data(
            preprocess_input=preprocess_input, parameters=parameters
        )

        model = create_model(architecture=model, parameters=parameters)

        history = fit(
            model=model,
            train_generator=train_generator,
            test_generator=test_generator,
            train_files=train_files,
            test_files=test_files,
            parameters=parameters,
        )

        score = evaluation_model(model, test_generator)

        acc = history.history["accuracy"]
        val_acc = history.history["val_accuracy"]

        loss = history.history["loss"]
        val_loss = history.history["val_loss"]

        plt.figure(figsize=(8, 8))
        plt.subplot(2, 1, 1)
        plt.plot(acc, label="Training Accuracy")
        plt.plot(val_acc, label="Validation Accuracy")
        plt.legend(loc="lower right")
        plt.ylabel("Accuracy")
        plt.ylim([min(plt.ylim()), 1])
        plt.title("Training and Validation Accuracy")

        plt.subplot(2, 1, 2)
        plt.plot(loss, label="Training Loss")
        plt.plot(val_loss, label="Validation Loss")
        plt.legend(loc="upper right")
        plt.ylabel("Cross Entropy")
        plt.ylim([0, 1.0])
        plt.title("Training and Validation Loss")
        plt.xlabel("epoch")
        plt.show()

        filename = f"{filename}_ferplus"

        if os.path.isfile(f"./logs/{filename}_parameters.log"):
            with open(f"./logs/{filename}_parameters.log", "r") as file:
                print(file.read())
                file.close()

        choice = input("save model? (O/N)\n>>>")

        if choice == "O":
            saveModel(filename=f"{filename}", model=model)
            with open(f"./logs/{filename}_parameters.log", "w") as file:
                file.write(f"{parameters}\nval_acc: {val_acc}\nval_loss: {val_loss}")
                file.close()
