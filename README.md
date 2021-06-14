# FlickrPhotos
A sample app that consumes Flickr API to retrieve a list of Images  and display it in the app .

## Project Architecture 
Applying Clean Architecture princples by seperating packages(Modules) into :
1. Presentation Layer : Implementing MVVM  as an Archeticture pattern with the help of Archeticrue Components .
2. Domain Layer : Managing Business logic by representing it as Use Cases that use  Repository pattern that consumes both server and database using  RxJava2 and RxAndroid2 .
3. Entities : Data Models used in the  project 

### Todos 
- Replace RxJava with coroutines Flow 
- Unit Tests
