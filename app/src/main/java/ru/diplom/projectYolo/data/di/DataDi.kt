package ru.diplom.projectYolo.data.di

import org.kodein.di.DI
import org.kodein.di.LazyDI
import ru.diplom.projectYolo.data.web.webDi

val dataDi: DI = LazyDI {
    DI {
        importOnce(webDi)
    }
}