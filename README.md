RecyclerViewLayout
===================================  

[ ![Download](https://api.bintray.com/packages/katelee/maven/RecyclerViewLayout/images/download.svg) ](https://bintray.com/katelee/maven/RecyclerViewLayout/_latestVersion)

Developing Project

## Table of Contents
* [Demo](#demo)
* [English info](#english-info)
* [中文介紹](#chinese-info)
* [Usage](#usage)

### Demo
![auto_hiding](https://raw.githubusercontent.com/kateLee/RecyclerViewLayout/master/images/auto_hiding.gif)
![parallax_scrolling](https://raw.githubusercontent.com/kateLee/RecyclerViewLayout/master/images/parallax_scrolling.gif)
![load_more](https://raw.githubusercontent.com/kateLee/RecyclerViewLayout/master/images/load_more.gif)

### English info
Base on android.support.v7.RecyclerView

#### Feature
1. support swipe to refresh and load more.
2. support use header and footer.
3. support header auto hiding.
4. support header parallax scrolling.

### Chinese info
以 android.support.v7.RecyclerView 作為基底
#### 特色
1. 支援下拉更新和載入更多。
2. 支援頁首和頁腳項目。
3. 支援在使用者滑動時，頁首自動隱藏。
4. 支援視差滾動的頁首。

### Usage
```
buildscript {
    repositories {
        jcenter()
    }
}

repositories {
    jcenter()
}

dependencies {
    compile 'com.github.katelee.widget:recyclerviewlayout:0.9.4'
}
```