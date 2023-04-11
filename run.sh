cd src/infra || exit
kubectl apply -f mongo-secrets.yml
kubectl apply -f user-client-configmap.yml
kubectl apply -f redis-configmap.yml
kubectl apply -f pg-kafka-secrets.yml

kubectl apply -f user-client-service.yml
kubectl apply -f mongo-service.yml
kubectl apply -f redis-service.yml
kubectl apply -f pg-kafka-service.yml

kubectl apply -f user-client-deployment.yml
kubectl apply -f mongo-stateful.yml
kubectl apply -f redis-stateful.yml
kubectl apply -f pg-kafka-stateful.yml

minikube service user-client