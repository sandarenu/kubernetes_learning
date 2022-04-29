# Learning Kubernetes

## Using local docker images

### Method 01

1. Configure minikube to use local docker `eval $(minikube -p minikube docker-env)`
2. Build the docker image
```
docker build -t sansoft/mysql:0.0.1 -f initial-data/Dockerfile.dev initial-data
```
3. To get an image from local docker, in the deployment file include `imagePullPolicy: Never`
```
      containers:
        - name: test-mysql
          image: test_kube/mysql
          imagePullPolicy: Never
```
### Method 02

1. Build docker image as usual. 
2. Load the image to minikube.
```
minikube image load <Image name with version>
```
Eg:
```
minikube image load sansoft/mysql:0.0.1
```
3. In kubernetes deployment file included `imagePullPolicy: Never`, refer above.


**NOTE** For more information refer https://levelup.gitconnected.com/two-easy-ways-to-use-local-docker-images-in-minikube-cd4dcb1a5379

## Port Forwarding

* To access running containers we have to do port forwarding.
```
kubectl port-forward <pod-name> 3306:3306 -n <your-namespace>
```
Eg: `kubectl port-forward mysql-deployment-bffbdc995-sbt4d 3306:3306` 


## Access Shell inside a Pod

`kubectl exec -it <pod-name> -- /bin/sh`

## Access internal APIs using Kubernetes proxy

This can be used to check on the internal apis. We normally don't open all the internal services to extenal world. Therefore this proxy capability can be used for debug purposes. 

* 1. Enable proxy
```
kubectl proxy --port=8080
```

http://localhost:8080/api/v1/namespaces/default/services/backend-app/proxy/hello

## Expose kubernetes service to outside world

To expose service to outside world, we should expose it via kubernetes **Service** of type **NodePort**. 

Eg:

```
apiVersion: v1
kind: Service
metadata:
  name: backend-app
spec:
  type: NodePort
  selector:
    app: backend-app
  ports:
  - port: 8080
    targetPort: 8080
    nodePort: 30080
```

After configuring and creating the serive you can access your APIs using kubernetes internal IP. Use following to find that internal IP.
```
> kubectl describe node minikube | grep InternalIP
InternalIP:  192.168.49.2
```
Once internal ip is found use that to access the API you want to access
Eg:
```
http://192.168.49.2:30080/hello
```

You can also use `minikube ip` to find this internal IP if you are running minikube.

## Tail logs inside a POD

```sh
kubctl logs --follow <POD Name>
```
