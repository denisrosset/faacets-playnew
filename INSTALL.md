## Install and run the Faacets website locally

The website is packaged using Docker containers.

### Prerequisites

Install SBT.

Install Docker: follow the instructions on the Docker website, including
"Manage Docker as a non-root user" in the post-installation steps.

### Download, compile and run the website

1) Clone the Git repository
2) Run `sbt docker:publishLocal` to create a local Docker container with the version
3) Run `docker run -e APPLICATION_SECRET=1234 --net="host" faacets-playnew:0.14.1.0-SNAPSHOT`
   where `1234` is the application secret (does not matter much, as all the website is public)
   and where `0.14.1.0-SNAPSHOT` is the version you want to run. By default, we run the networking
   on the host (i.e. the port 9000 will be opened on the host).
4) Connect to http://localhost:9000
