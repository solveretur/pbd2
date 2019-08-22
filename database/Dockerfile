FROM library/mysql:5.7.24

COPY setup.sql /docker-entrypoint-initdb.d/
COPY init.sql /docker-entrypoint-initdb.d/

COPY data/* /var/lib/mysql-files/