# Домашнее задание к занятию «3.2. Работа с мультимедиа»

## Мини-проект. Single Album App

### Описание

Реализуйте приложение, предназначенное для воспроизведения одного музыкального альбома. Адаптированный скриншот сервиса Яндекс Музыка — вам нужно сделать по его аналогии:

![](https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/pic/yandex-music.png)

URL, с которого можно получать JSON с нужными данными: https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json (данные любезно предоставлены [SoundHelix](https://www.soundhelix.com)).

URL для композиции можно построить из BASE_URL: https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/ + имя файла (поля file из [album.json](https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json)).

На экране должен быть `RecyclerView` с композициями и общие элементы управления — информация об альбоме и кнопки. При этом вам нужно реализовать логику воспроизведения. Берите за основу [логику веб-версии Яндекс Музыки](https://music.yandex.ru/artist/36800/albums).

В проекте не должно быть неиспользуемых зависимостей. 

Вы сами решаете, насколько функциональным сделать ваше приложение. Минимальная функциональность:
1. При клике на кнопку Play она заменяется на кнопку Pause.
1. После того как воспроизведение текущей композиции заканчивается, начинается воспроизведение следующей. После последней воспроизводится первая.
1. `RecyclerView` нормально переиспользует `ViewHolder` — ничего не ломается и состояние не теряется.
