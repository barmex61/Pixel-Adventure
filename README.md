# pixeladventure

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a Kotlin project template that includes Kotlin application launchers and [KTX](https://libktx.github.io/) utilities.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3.
- `android`: Android mobile platform. Needs Android SDK.
- `ios`: iOS mobile platform using RoboVM.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.
- `teavm`: Experimental web platform using TeaVM and WebGL.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/lib`.
- `lwjgl3:run`: starts the application.
- `teavm:build`: builds the JavaScript application into the build/dist/webapp folder.
- `teavm:run`: serves the JavaScript application at http://localhost:8080 via a local Jetty server.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

// In app screenshots //

![ninja4](https://github.com/user-attachments/assets/5e351420-f594-4c4d-a6a4-4e485c61310b)
![ninja3](https://github.com/user-attachments/assets/7be506f8-ac93-4313-a6c0-8aa03face9c1)
![ninja2](https://github.com/user-attachments/assets/163529c9-e683-4335-ab40-4aad609f3d43)
![ninja1](https://github.com/user-attachments/assets/2b426e93-1877-42ea-ace0-ec1596f45ea6)
![ninja10](https://github.com/user-attachments/assets/1dd7f473-65b9-4d08-9410-7e5451b48d60)
![ninja9](https://github.com/user-attachments/assets/9faf04e7-6fd2-40f4-b538-f118d4452625)
![ninja8](https://github.com/user-attachments/assets/623f9350-8e4e-41f5-8117-bf59fe935d0c)
![ninja7](https://github.com/user-attachments/assets/1930456b-80fe-45c7-a3e2-aa8b699fe92a)
![ninja6](https://github.com/user-attachments/assets/85c30b4d-51a9-4f4b-a152-80be626d9f9d)
![ninja5](https://github.com/user-attachments/assets/158e4afc-26c4-47a5-82e6-20b3f224b782)
