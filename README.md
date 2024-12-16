<div align="center">
  <h3 align="center">Image Slideshow Manager</h3>
  <p align="center">
    A Java Spring Boot application for managing image slideshows with proof-of-play notifications and modern backend architecture.
    <br />
    <a href="#about-the-project"><strong>Explore the docs »</strong></a>
    <br />
    <br />
  </p>
</div>

---

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#api-documentation">API Documentation</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
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

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

- **Java Spring Boot**: Backend framework.
- **Spring WebFlux**: Reactive programming.
- **Spring Data JPA**: Database interaction.
- **PostgreSQL**: Persistent data storage.
- **Docker & Docker Compose**: Containerization.

<p align="right">(<a href="#top">back to top</a>)</p>

---

## Getting Started

### Prerequisites

- **Java 17+** installed.
- **Maven** installed for dependency management.
- **Docker** and **Docker Compose** installed for containerization.
- Access to PostgreSQL instance.

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/njdos/task_slideshow.git
   cd image-slideshow-manager
   ```

2. Build the project:
   ```sh
   mvn clean install
   ```

3. Run the application using Docker Compose:
   ```sh
   docker-compose up --build
   ```

### API Documentation

* Swagger API Documentation: http://localhost:8080/swagger-ui/index.html
* Application APIs: http://localhost:8080


