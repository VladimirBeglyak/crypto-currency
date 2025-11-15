# Вывод пути к файлу
output "created_file_path" {
  description = "Путь к созданному файлу"
  value       = local_file.example.filename
}

# Вывод содержимого файла
output "created_file_content" {
  description = "Содержимое файла"
  value       = local_file.example.content
}
