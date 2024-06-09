# Tell Terraform to include the hcloud provider
terraform {
  required_providers {
    hcloud = {
      source = "hetznercloud/hcloud"
      # Here we use version 1.45.0, this may change in the future
      version = "1.45.0"
    }
  }
}

# Declare the hcloud_token variable from .tfvars
variable "dev_hcloud_token" {
  sensitive = true
}

# Configure the Hetzner Cloud Provider with your token
provider "hcloud" {
  token = var.dev_hcloud_token
}

resource "hcloud_network" "dev_private_network" {
  name     = "dev-kubernetes-cluster"
  ip_range = "10.0.0.0/16"
}

resource "hcloud_network_subnet" "dev_private_network_subnet" {
  type         = "cloud"
  network_id   = hcloud_network.dev_private_network.id
  network_zone = "eu-central"
  ip_range     = "10.0.1.0/24"
}

resource "hcloud_server" "dev-main-node" {
  name        = "dev-main-node"
  image       = "ubuntu-22.04"
  server_type = "cax11"
  location    = "fsn1"
  public_net {
    ipv4_enabled = true
    ipv6_enabled = true
  }
  network {
    network_id = hcloud_network.dev_private_network.id
    # IP Used by the main node, needs to be static
    # Here the worker nodes will use 10.0.1.1 to communicate with the main node
    ip = "10.0.1.1"
  }
  user_data = file("${path.module}/cloud-init.dev.yaml")

  # If we don't specify this, Terraform will create the resources in parallel
  # We want this node to be created after the private network is created
  depends_on = [hcloud_network_subnet.dev_private_network_subnet]
}

resource "hcloud_server" "dev-worker-node-1" {
  name        = "dev-worker-node-1"
  image       = "ubuntu-22.04"
  server_type = "cax11"
  location    = "fsn1"
  public_net {
    ipv4_enabled = true
    ipv6_enabled = true
  }
  network {
    network_id = hcloud_network.dev_private_network.id
  }
  user_data = file("${path.module}/cloud-init-worker.dev.yaml")

  # add the main node as a dependency
  depends_on = [hcloud_network_subnet.dev_private_network_subnet, hcloud_server.dev-main-node]
}
