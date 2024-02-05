# 3rd-sc-ex1-CitySavior-Android


## Tech Stack
- `Jetpack Compose`<br>
- `Jetpack viewmodel`<br>
- `OkHttp3` `Retrofit2`<br>
- `dagger-hilt`<br>
- `toml`<br>

## Package Structure
Considering the app's small size and a single Android developer, the project has been organized into four key layers: App, Data, Domain, and Presentation.

### App
Responsible for the application and serves as the initial entry point for the app.
- Handles Dependency Injection (DI).
- Manages the logic for determining which top destination in the presentation layer to navigate to when the app is first launched.

### Data
Consists of api, dto, and repositoryImpl.
- Utilizes functions that convert api responses into repository data.
- DTO corresponds to the model and is transformed using the toDomain function.

### Domain
Comprises model, repository, and params.
- Uses params when repository function arguments become extensive and includes convenience methods for optimistic responses.
- Usecase is not employed except for authentication due to the absence of complex business logic in the app.
- Fixture methods are created for the model.
- Utilizes the Async class as a wrapping class for API responses.

### Presentation
Structured with view, viewModel, component, and navigation.
- Organizes the package structure based on screen routers.
- Each page has its own view, viewModel, and component, and the pages are organized in a nested format.
