<div align="center">
    <h3 align="center">Image Slideshow Manager</h3>
    <p align="center">
        A Java Spring Boot application for managing image slideshows with proof-of-play notifications and modern backend architecture. <br />
        <a href="#about-the-project"><strong>Explore the docs »</strong></a> <br />
        <br />
    </p>
</div>


---

<details>
    <summary>Table of Contents</summary>
    <ol>
        <li>
            <a href="#about-the-project">About The Project</a>
            <ul>
                <li><a href="#built-with">Built With</a></li>
            </ul>
        </li>
        <li>
            <a href="#getting-started">Getting Started</a>
            <ul>
                <li><a href="#prerequisites">Prerequisites</a></li>
                <li><a href="#running-the-project-locally">Running the Project Locally</a></li>
                <li><a href="#running-with-docker-compose">Running with Docker Compose</a></li>
                <li><a href="#using-docker-hub-image">Using Docker Hub Image</a></li>
            </ul>
        </li>
        <li><a href="#api-documentation">API Documentation</a></li>
        <li><a href="#usage">Usage</a></li>
        <li><a href="#contributing">Contributing</a></li>
        <li><a href="#contact">Contact</a></li>
    </ol>
</details>


---

## About The Project

The **Image Slideshow Manager** is a backend application designed to manage a list of image URLs and enable slideshow playback with transitions. Key features include:

- RESTful APIs to manage images and slideshows.
- Proof-of-play notification for tracking slideshow events.
- Persistent data storage using PostgreSQL.
- Built using modern Java techniques, such as reactive programming (WebFlux).
- Fully containerized for easy deployment.

### Quick Start

To explore the full functionality of the application, simply navigate to the root URL (http://localhost:8080/). 
The frontend interface will allow you to interact with all the features and see how everything works seamlessly. You can manage images, create slideshows, and track transitions directly from the user-friendly interface.

---

### Built With

- **Java Spring Boot**: Backend framework.
- **Spring WebFlux**: Reactive programming.
- **Spring Data JPA**: Database interaction.
- **PostgreSQL**: Persistent data storage.
- **Docker & Docker Compose**: Containerization.


---

## Getting Started

### Prerequisites

- **Java 17+** installed.
- **Maven** installed for dependency management.
- **Docker** and **Docker Compose** installed for containerization.
- Access to PostgreSQL instance.

---

### Running the Project Locally

1. Clone the repository:
   ```sh
   git clone https://github.com/njdos/task_slideshow.git
   ```

2. Configure application.properties:
   Update the src/main/resources/application.properties file with the following settings for local database access:
   ```sh
   spring.application.name=task_slideshow
   task.slideshow.version=v1
   
   spring.r2dbc.url=r2dbc:postgresql://localhost:5432/slideshow_db
   spring.r2dbc.username=username
   spring.r2dbc.password=password
   ```

3. Build the project::
   ```sh
   mvn clean install
   ```

4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

5. Access the API: Open http://localhost:8080/swagger-ui in your browser to explore the API documentation.

---

### Running with Docker Compose

1. Use the same `.env` file: Ensure the `.env` file in the root directory is properly configured with the following content:
   ```sh
   SPRING_R2DBC_URL=r2dbc:postgresql://db:5432/slideshow_db
   SPRING_R2DBC_USERNAME=username
   SPRING_R2DBC_PASSWORD=password
   
   KAFKA_BROKER_ADDRESS=kafka:9092
   ```

2. Start the application: Run the following command to build and start the project in Docker:
   ```sh
   docker-compose up --build
   ```

---

### Using Docker Hub Image

If you prefer not to build the project locally, you can use the prebuilt Docker image available on Docker Hub.

1. Pull the image:
   ```sh
   docker pull njdos/task_slideshow:latest
   ```

2. Run the container:
   ```sh
   docker run --name task_slideshow -p 8080:8080 --env-file .env njdos/task_slideshow:latest
   ```

---

### API Documentation

* Swagger API Documentation: http://localhost:8080/swagger-ui/index.html
* Application APIs / Frontend: http://localhost:8080

---

### Usage

Explore the API to:

* Add, delete, and search for images.
* Create and manage slideshows.
* Track proof-of-play notifications.

---

### Contributing

Contributions are welcome! Please open an issue to discuss your ideas.
