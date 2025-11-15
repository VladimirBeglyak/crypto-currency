terraform {
  required_version = ">= 1.0"
}

# Локальный провайдер для учебного примера
provider "local" {

}

# Ресурс — создаёт файл с содержимым
resource "local_file" "example" {
  filename = "${path.module}/${var.filename}"
  content  = var.file_content
}
