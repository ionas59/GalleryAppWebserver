## Project Status

This project is currently paused. 
The core backend functionality for image upload and metadata storage is implemented and functional.

## Future Ideas

- Implement a **React frontend** to display the gallery.
- Support **tag-based sorting and filtering** of images ( already implemented in the backend ).

- (Optional) Add basic user auth for access control.

## Notes

- Image metadata is being stored in the sql db including md5 hashes.
- Images are sent via POST and saved if md5 doesn't exist yet.
- Kotlin Android app currently handles sync/upload.
- Spring Boot backend stores files and SQL metadata correctly.

## Learnings

- Designing and using **interfaces**.
- Writing and handling **asynchronous tasks** (e.g., fetch images asynchronously ).
- Implementing **callback functions** for communication between Android components.
- Building **RecyclerViews** to manage dynamic image lists.
- Using **Retrofit** for network communication between Android and backend.

This project may be revisited later depending on time and alignment with other goals.

Thank you for taking interest in this project.
