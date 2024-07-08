import contextlib
import os

import cv2
from flask import Flask, render_template, Response, request, flash, redirect
from emoji import emojize
from keras.src.applications.resnet import ResNet50
from keras.src.ops import expand_dims
from keras.src.utils import load_img, img_to_array
from werkzeug.utils import secure_filename
from emotion_recognition.prediction import get_face_from_frame, get_emotions_from_face, get_sorted_results
from emotion_recognition.Models.common_functions import (
    create_model,
    get_data,
    fit,
    evaluation_model,
    saveModel,
)

switch, out, capture, rec_frame = (
    1,
    0,
    0,
    0,
)

face_shape = (80, 80)


# ## Load model
# model = load_model("./emotion_recognition/Models/trained_models/resnet50_ferplus")

## Load weights
parameters = {
    "shape": [80, 80],
    "nbr_classes": 7,
    "batch_size": 8,
    "epochs": 50,
    "number_of_last_layers_trainable": 10,
    "learning_rate": 0.001,
    "nesterov": True,
    "momentum": 0.9,
}
model = create_model(architecture=ResNet50, parameters=parameters)
model.load_weights("emotion_recognition/Models/trained_models/resnet50_ferplus.h5")


class_cascade = cv2.CascadeClassifier(
    "./emotion_recognition/ClassifierForOpenCV/frontalface_default.xml"
)
face = None
emotions = None

# instatiate flask app
app = Flask(__name__, template_folder="./templates", static_folder="./staticFiles")


UPLOAD_FOLDER = '/Users/user/Desktop/DiplomIndira/server/server/emotion_recognition/'
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

emotions_with_smiley = {
    "happy": f"{emojize(':face_with_tears_of_joy:')} HAPPY",
    "angry": f"{emojize(':pouting_face:')} ANGRY",
    "fear": f"{emojize(':fearful_face:')} FEAR",
    "neutral": f"{emojize(':neutral_face:')} NEUTRAL",
    "sad": f"{emojize(':loudly_crying_face:')} SAD",
    "surprise": f"{emojize(':face_screaming_in_fear:')} SURPRISE",
    "disgust": f"{emojize(':nauseated_face:')} DISGUST",
}



def magnify_emotion(emotion):
    return f"<p>{emotions_with_smiley[emotion[0]]} :{int(emotion[1] * 100)} %</p>"


def magnify_results(emotions):
    return "".join(list(map(magnify_emotion, emotions)))


@app.route("/")
def index():
    return render_template("index.html")




@app.route("/uploadImage", methods=['POST'])
def uploadImage():
    def allowed_file(filename):
        return ('.'
                in filename and filename.rsplit('.', 1)[1].lower()
                in ALLOWED_EXTENSIONS)

    app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
    if request.method == 'POST':
        # check if the post request has the file part
        if 'files' not in request.files:
            flash('No file part')
            return redirect(request.url)
        file = request.files['files']
        # if user does not select file, browser also
        # submit a empty part without filename
        if file.filename == '.':
            flash('No selected file')
            return redirect(request.url)
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

    img = load_img(
        path= f"{UPLOAD_FOLDER}/{filename}",
        target_size = face_shape
    )
    img_array = img_to_array(img)
    img_array = expand_dims(img_array, 0)  # Create a batch

    test = get_sorted_results(model.predict(x = img_array))

    return Response(magnify_results(test))


if __name__ == "__main__":
    app.run(host="192.168.0.10", port=5000)

