terraform {
  # backend "s3" {
  #   bucket = "{YOUR_S3_BUCKET_NAME}"
  #   key    = "terraform/backend"
  #   region = "{YOUR_AWS_REGION}"
  # }
}

locals {
  env_name         = "sandbox"
  # aws_region       = "{YOUR_AWS_REGION}"
  k8s_cluster_name = "ms-cluster"
}
