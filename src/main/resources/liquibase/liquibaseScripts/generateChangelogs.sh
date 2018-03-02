liquibase --driver=com.mysql.jdbc.Driver\
          --classpath=mysql-connector-java-5.1.45-bin.jar \
          --changeLogFile=./structure.xml \
          --url="jdbc:mysql://127.0.0.1:3306/niklas" \
          --username=root \
          --password=Start123 \
          generateChangeLog