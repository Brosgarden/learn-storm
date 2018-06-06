#!/usr/bin/env bash
apt-get update
apt-get install -y unzip supervisior openjdk-8-jre

systemctl stop supervisor

groupadd storm
useradd --gid storm --home-dir /home/storm --create-home --shell /bin/bash storm

wget http://apache.claz.org/storm/apache-storm-1.2.1/apache-storm-1.2.1.tar.gz