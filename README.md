# Address_Book_app

This is a project that builds an app that allows the user to scroll through all the comics ever released and view details for each comic.

**Application**

Polished UI, with thought put into the user experience.

Android app which make use of Marvel Comics API to get comics details and display the information accordingly.

**Separation of Concerns**

The Project Follows MVVM Architecture Pattern in decoupling the logic from the Activity to a ViewModel Class that extends ViewModel.


Design pattern used - MVVM (Model-View-ViewModel), ViewModel, Repository pattern, and Android Recommended App Architecture


This App uses following TechStack : 

-- Kotlin Library
-- Room Database
-- Dagger Hilt
-- Recyclerviews
-- Android Architechure Component(ViewModel and LiveData)
-- Retrofit and Flow
-- JUnit4 Library For Testing


## Project file

data folder contain everything relating to the data which includes the database, Model, network calls via retrofit class to make request to the server

view folder contains activity and their adapter- to hold ui

viewmodels folder contain MainViewModel to give any activity that want to observe changes in life cycle.

util folder contains the NetworkBoundResource, Resource, and Extention Files

di folder contains the AppModule for Dependency injection of the various component



## App Demo / Video

https://user-images.githubusercontent.com/43482405/117624870-4e692f80-b16d-11eb-8166-a1ca56acd49c.mp4



**App Images**

![imagemarvelhome](https://user-images.githubusercontent.com/43482405/117613571-02af8980-b15f-11eb-90d8-c5f263a31cc7.jpeg)

![imagemarveldetail](https://user-images.githubusercontent.com/43482405/117613584-080cd400-b15f-11eb-856a-84f439961f7d.jpeg)