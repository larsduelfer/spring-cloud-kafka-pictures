FROM mcr.microsoft.com/mssql/server:2017-latest-ubuntu

#Copied from:
#https://github.com/microsoft/mssql-docker/blob/master/linux/mssql-tools/Dockerfile

RUN apt-get update && ACCEPT_EULA=Y apt-get install -y msodbcsql mssql-tools
RUN echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bashrc
RUN /bin/bash -c "source ~/.bashrc"

RUN apt-get -y install locales
RUN locale-gen en_US.UTF-8
RUN update-locale LANG=en_US.UTF-8

#Copied from https://github.com/mcmoe/mssqldocker:
RUN mkdir -p /usr/config
WORKDIR /usr/config

COPY configure-db.sh /usr/config
COPY entrypoint.sh /usr/config
COPY setup.sql /usr/config

RUN chmod +x /usr/config/entrypoint.sh
RUN chmod +x /usr/config/configure-db.sh

ENTRYPOINT ["./entrypoint.sh"]

# Tail the setup logs to trap the process
CMD ["tail -f /dev/null"]
