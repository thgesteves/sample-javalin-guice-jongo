# sample-javalin-guice-jongo
Sample Java project using Javalin + Guice + Jongo

### API
curl -v --location --request POST 'http://localhost:9080/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "bart",
    "fullname": "Bart Simpson"
}'

curl --location --request GET 'http://localhost:9080/user/bart'

curl --location --request DELETE 'http://localhost:9080/user/bart'