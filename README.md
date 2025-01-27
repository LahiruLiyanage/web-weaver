# WebWeaver

The **WebWeaver** server listens on port 80 and handles incoming HTTP requests. It parses the request headers, validates the HTTP method, and serves the appropriate content. If the requested resource is not found or the method is unsupported, the server responds with an error page. By modifying the system's /etc/hosts file or using a proxy configuration, requests to http://google.com are redirected to the WebWeaver server.

---

## Features

- **Custom HTTP Request Handling**: Processes GET, POST, and other HTTP methods.
- **URL Redirection**: Redirects `http://google.com` to the WebWeaver server when running.
- **Dynamic File Serving**: Serves files based on incoming requests and resource paths.
- **Error Handling**: Responds with appropriate HTTP error codes (e.g., 400, 404, 405).
- **Platform Compatibility**: Built and tested on Ubuntu.
- **Educational Purpose**: Ideal for understanding and demonstrating HTTP server concepts.

---

## How It Works

When the WebWeaver server is running, it intercepts and processes incoming HTTP requests. By modifying the system's `/etc/hosts` file or using a proxy configuration, requests to `http://google.com` are redirected to the WebWeaver server, which responds with custom content.

---

## Getting Started

### Prerequisites

- **Operating System**: Ubuntu (or compatible Linux distribution).
- **Java Development Kit (JDK)**: Version 8 or later.
- **Basic Networking Knowledge**: Understanding of DNS and HTTP.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/LahiruLiyanage/web-weaver.git
   cd web-weaver
   ```

2. Compile the server:
   ```bash
   javac -d bin src/*.java
   ```

3. Run the server:
   ```bash
   java -cp bin web-weaver
   ```

4. Configure redirection:
    - Edit your `/etc/hosts` file:
      ```
      sudo gedit /etc/hosts
      ```
    - Add the following line:
      ```
      127.0.0.1 google.com
      ```
    - Save and exit.

5. Open your browser and navigate to `http://google.com` to see WebWeaver in action.

---

## Project Structure

```
WebWeaver/
├── src/           # Source code
├── bin/           # Compiled classes
├── README.md      # Project documentation
├── LICENSE        # License information
```

---

## License

This project is licensed under the [MIT License](LICENCE.txt).

---

## Contributing

Contributions are welcome! Feel free to fork the repository, make improvements, and submit a pull request.

---

## Contact

For any questions or feedback, please reach out via [LinkedIn](https://www.linkedin.com/in/liyanage-lahiru/).

---

## Acknowledgments

Special thanks to the open-source community for providing tools and inspiration for this project.

