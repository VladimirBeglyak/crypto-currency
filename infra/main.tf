terraform {
  backend "s3" {
    bucket = "vladimir-crypto-currency"
    key    = "terraform/backend"
    region = "eu-north-1"
  }
}

locals {
  env_name         = "sandbox"
  aws_region       = "eu-north-1"
  k8s_cluster_name = "ms-cluster"
}
