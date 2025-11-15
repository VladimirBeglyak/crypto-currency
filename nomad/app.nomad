job "java-app" {
  datacenters = ["dc1"]
  type = "service"

  group "app" {
    task "app" {
      driver = "docker"
      config {
        image = "IMAGE_REPLACE"
        ports = ["http"]
      }

      resources {
        cpu    = 500
        memory = 256
      }

      service {
        name = "java-app"
        port = "http"
      }
    }

    network {
      port "http" {
        static = 8080
      }
    }
  }
}
