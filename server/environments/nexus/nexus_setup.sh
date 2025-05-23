#!/bin/bash

# Assumes volume is attached to an EC2 instance and
# the EC2 host machine volume is formatted. 

# Because the same EC2 instance isn't reused 
# we must manually create the mount point directory
# and mount on setup. 

echo "          --- Beginning Nexus setup ---"
docker compose up -d 

container_volume_path="/mnt/nexus-data"
host_volume_path="/dev/xvdd" 

# Create the mount point directory for the container volume
sudo mkdir -p $container_volume_path

# Mount the host volume to the container volume path
sudo mount $host_volume_path $container_volume_path

df -h | grep $container_volume_path

echo "          --- Nexus setup complete ---"