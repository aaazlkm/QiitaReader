# QiitaReader アプリ

以下に Apk が置いてあります、
ぜひインストールして見てください！

TODO url 置く

## 概要

このアプリは Qiita の記事を見るためのアプリです
いくつかのカテゴリで Qiita の記事を取得し見ることができます
また、記事保存機能も実装しており事前にダウンロードすることでオフライン環境でも記事を見ることができます

| 画面 | リロード処理 | 保存処理 |
| :-------: | :----------: | :-------: |
| ![画面遷移](https://raw.githubusercontent.com/wiki/aaazlkm/QiitaApp/screen_recording_pager.gif) | ![画面遷移](https://raw.githubusercontent.com/wiki/aaazlkm/QiitaApp/screen_recording_reload.gif) | ![画面遷移](https://raw.githubusercontent.com/wiki/aaazlkm/QiitaApp/screen_recording_save.gif) |

** 留意事項 **

アクセストークン取得の機能を実装していないので IP アドレスごとに 1 時間に 60 回までしかリクエストを投げれません
https://qiita.com/api/v2/docs#%E5%88%A9%E7%94%A8%E5%88%B6%E9%99%90

## 技術スタック

- kotlin
- MVVM + CleanArchitecture
- rxjava
- dagger
- retrofit
- room

## プロジェクトの構造

- 全体の構造
  - 以下の三つの層からなるレイヤードアーキテクチャを採用している
    - presentation 層
    - domain 層
    - infra 層
  - 大まかな役目としては
    - infra 層で生のデータを取得し
    - domain 層で生のデータをアプリ用に加工し
    - presentation 層で加工したデータを表示する
  - それぞれの層の繋ぎ目を RxJava によって繋いでいる
- presentation 層
  - 画面に関しての処理を行う層
  - MVVM アーキテクチャを採用しており基本的に一画面につき以下がペアになる
    - Fragment
    - ViewModel
- domain 層
  - アプリのドメインロジックについて実装する層
  - presentation層とinfra層の仲介役を担う
  - 具体的な処理としてはinfra 層で取得したデータを presentation 層で使う形(= アプリで使用する形)に加工するなど
- infra 層
  - アプリで使用するデータを取得する層
  - API や DB からデータを取得する
  - この層では特にデータの加工などの処理は行わないで取得したデータをそのまま流す(加工などの処理は domain 層で行う)

## 使用ライブラリ

- [android jetpack](https://developer.android.com/jetpack/)
  - Foundation
    - AppCompat
    - Android KTX
    - Test
  - Architecture
    - DataBinding
    - ViewModel
    - Room
  - UI
    - constraintlayout
    - swiperefreshlayout
    - recyclerview
- [kotlin](https://kotlinlang.org/)
- [Material Components for Android](https://github.com/material-components/material-components-android)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [RxKotlin](https://github.com/ReactiveX/RxKotlin)
- [Gson](https://github.com/google/gson)
- [ktlint](https://github.com/pinterest/ktlint)
- [Timber](https://github.com/JakeWharton/timber)
- [Dagger](https://github.com/google/dagger)
- [Retrofit](https://github.com/square/retrofit)
- [Markwon](https://github.com/noties/Markwon)
