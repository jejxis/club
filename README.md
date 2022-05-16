# club

application.properties에 대하여

spring.jpa.hibernate.ddl-auto=update 로 했더니 맨 처음에 오류나서
spring.jpa.hibernate.hbdm2ddl-auto=update로 했다가 club_member 테이블이 안 만들어져서
spring.jpa.hibernate.ddl-auto=create로 했더니 테이블이 잘 만들어졌고
spring.jpa.hibernate.ddl-auto=update로 다시 바꿨더니 이제 오류가 안 난다.
