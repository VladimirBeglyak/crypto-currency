job "java-app" {
  datacenters = ["dc1"]
  type = "service"

  group "java" {
    count = 1

    task "app" {
      driver = "docker"

      config {
        image = "IMAGE_REPLACE"
        ports = ["http"]
      }

      resources {
        cpu    = 500
        memory = 512
      }

      service {
        name = "java-app"
        port = "http"

        check {
          type     = "http"
          path     = "/actuator/health"
          interval = "10s"
          timeout  = "2s"
        }
      }
    }

    network {
      port "http" {
        static = 8080
      }
    }
  }
}
