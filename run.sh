cd src/infra || exit
kubectl apply -f mongo-secrets.yml
kubectl apply -f user-client-configmap.yml

kubectl apply -f user-client-service.yml
kubectl apply -f mongo-service.yml

kubectl apply -f user-client-deployment.yml
kubectl apply -f mongo-stateful.yml

minikube service user-client