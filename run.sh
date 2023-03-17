cd src/infra || exit
kubectl apply -f pg-secrets.yml
kubectl apply -f user-client-configmap.yml

kubectl apply -f user-client-service.yml
kubectl apply -f pg-service.yml

kubectl apply -f user-client-deployment.yml
kubectl apply -f pg-stateful.yml

minikube service user-client