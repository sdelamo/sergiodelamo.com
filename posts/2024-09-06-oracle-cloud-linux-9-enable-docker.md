---
title: Install Docker on Oracle Cloud Linux 9 
summary:  Instructions to install Docker on Oracle Cloud Linux 9
date_published: 2024-09-06T13:36:28+01:00
keywords:docker,oracle-linux
---

# [%title]

I have been trying to install [Writebook](https://once.com/writebook) in an Oracle Cloud instance with Oracle Linux 9. The installer could not install Docker. 

To install it manually, I  followed this [step-by-step guide](https://collabnix.com/how-to-install-docker-on-oracle-linux-a-step-by-step-guide/)

## Update System Packages

Open a terminal and update your system packages as you see below:

```
sudo yum update -y
```

## Add Docker Repository

Add Docker’s official repository to your system’s yum sources list as you see in the image below:

```
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

## Install Docker Engine

Install Docker Engine, CLI, and contained:

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo yum install docker-ce docker-ce-cli containerd.io
```

## Start Docker Service

Enable and start the Docker service:

```
sudo systemctl enable docker
sudo systemctl start docker
```
