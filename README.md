# meli-cbt-payment

API de criação e processamento de pagamentos.

## Instruções

Para executar a api é necessário uma instância de **MariaDB** que pode ser levantada conforme o comando abaixo:
```
docker run -d -p 3306:3306 --name meli --env MARIADB_USER=meli --env MARIADB_PASSWORD=melipass --env MARIADB_ROOT_PASSWORD=rootpass --env MARIADB_DATABASE=cbt_payment --env MARIADB_ROOT_PASSWORD=rootpass mariadb:latest
```
Após a inicialização da instância de banco de dados, executar o seguinte comando na pasta raiz do projeto:
```
./gradlew clean bootRun
```
A collection para teste dos recursos está presente na pasta **collection/**


## Diagrama de Estados de um Pagamento
![Screenshot_20220314_223741](https://user-images.githubusercontent.com/44138536/158288746-8583b27b-e386-4338-a5a3-87945487cb48.png)

## coverage
![Screenshot_20220314_222047](https://user-images.githubusercontent.com/44138536/158287526-057f4246-f049-4e3f-8197-9f538e2415a2.png)
