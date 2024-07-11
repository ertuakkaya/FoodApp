# FoodApp

FoodApp is a modern Android application for food ordering, built with Jetpack Compose and following clean architecture principles. It showcases the use of latest Android development technologies and best practices.

![App Screenshot](https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss1.jpg)

<table>
  <tr>
    <td><img src="[https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss1.jpg](https://github.com/ertuakkaya/FoodApp/blob/master/Screenshots/Screenshot_20240711_193003.png)" width="200"/></td>
    <td><img src="https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss2.jpg" width="200"/></td>
    <td><img src="https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss3.jpg" width="200"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss4.jpg" width="200"/></td>
    <td><img src="https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss5.jpg" width="200"/></td>
    <td><img src="https://github.com/ertuakkaya/FoodApp/raw/main/app/src/main/res/drawable/ss6.jpg" width="200"/></td>
  </tr>
</table>


## Features

- Browse a variety of food items
- View detailed information about each dish
- Add items to cart with quantity selection
- Manage cart items (view, update quantity, remove)
- User authentication with email and password
- Persistent login state
- Clean and intuitive UI with Jetpack Compose

## Technologies Used

- **Jetpack Compose**: For building the UI
- **Kotlin**: Programming language
- **MVVM Architecture**: For separation of concerns
- **Clean Architecture**: For scalable and maintainable code structure
- **Coroutines**: For asynchronous programming
- **Flow**: For reactive programming
- **Dagger Hilt**: For dependency injection
- **Retrofit**: For network requests
- **OkHttp**: For efficient HTTP requests
- **Coil**: For image loading
- **Firebase Authentication**: For user authentication
- **Navigation Component**: For in-app navigation
- **Gson**: For JSON parsing

## Project Structure

The project follows a clean architecture approach with the following main packages:

- `data`: Contains repositories, data sources, and models
- `di`: Dependency injection modules
- `domain`: Use cases and domain models
- `presentation`: UI-related components (screens, view models)
- `util`: Utility classes and functions

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/ertuakkaya/FoodApp.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or physical device.

## Configuration

To run this project, you need to set up Firebase for authentication:

1. Create a Firebase project and add your Android app to it.
2. Download the `google-services.json` file and place it in the `app` directory.
3. Enable Email/Password authentication in your Firebase console.

## API Usage

The app uses the following API endpoints:

- Get all foods: `http://kasimadalan.pe.hu/yemekler/tumYemekleriGetir.php`
- Get cart items: `http://kasimadalan.pe.hu/yemekler/sepettekiYemekleriGetir.php`
- Add to cart: `http://kasimadalan.pe.hu/yemekler/sepeteYemekEkle.php`
- Remove from cart: `http://kasimadalan.pe.hu/yemekler/sepettenYemekSil.php`
- Food images: `http://kasimadalan.pe.hu/yemekler/resimler/[food_name].png`

## Contributing

Contributions are welcome! If you'd like to contribute:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please ensure your code follows the project's coding standards and includes appropriate tests.

## License

This project is open source and available under the [MIT License](LICENSE).

## Contact

Ertugrul Akkaya - [GitHub](https://github.com/ertuakkaya)

Project Link: [https://github.com/ertuakkaya/FoodApp](https://github.com/ertuakkaya/FoodApp)

## Acknowledgements

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Dagger Hilt](https://dagger.dev/hilt/)
- [Retrofit](https://square.github.io/retrofit/)
- [Coil](https://coil-kt.github.io/coil/)
