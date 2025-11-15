# Имя создаваемого файла
variable "filename" {
  description = "Имя файла, который создаёт Terraform"
  type        = string
  default     = "hello.txt"
}

# Содержимое файла
variable "file_content" {
  description = "Содержимое файла"
  type        = string
  default     = "Hello from Terraform!"
}
