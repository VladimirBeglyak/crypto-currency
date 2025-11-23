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

module "aws-network" {
  source = "github.com/VladimirBeglyak/module-aws-network"

  env_name              = local.env_name
  vpc_name              = "msur-VPC"
  cluster_name          = local.k8s_cluster_name
  aws_region            = local.aws_region
  main_vpc_cidr         = "10.10.0.0/16"
  private_subnet_a_cidr = "10.10.0.0/18"
  private_subnet_b_cidr = "10.10.64.0/18"
  public_subnet_a_cidr  = "10.10.128.0/18"
  public_subnet_b_cidr  = "10.10.192.0/18"
}
