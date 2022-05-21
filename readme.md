# Learning Kubernetes

## Install Minikube

Once minikube is installed add following alias to your bash profile. It will allow you to run `kubectl` commands without minikube prefix.

```
alias kubectl="minikube kubectl --"
```

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

## Redeploy new images during testing

During testing, you may be updating the image and re-deploying without changing the image version. 
In such cased you'll first have to remove the image from minikube and re-load them. 

Steps to follow. 
1. Delete the deployment (Eg: `kubectl delete -f <YML>`)
2. Remove the image (Eg: `minikube image rm sansoft/test-kube-backend:0.0.1 `)
3. Build the fresh image (Eg: `docker build -t ....`)
4. Re-load the image (Eg: `minikube image load sansoft/test-kube-backend:0.0.1` )
5. Apply the deployment (Eg: `kubectl apply -f <YML>`)


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

After configuring and creating the serice you can access your APIs using kubernetes internal IP. Use following to find that internal IP.
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

### Another way to get Service URL

`minikube service <Service Name> --url`

Eg: `minikube service app-0 --url`

## Tail logs inside a POD

```sh
kubectl logs --follow <POD Name>
```

## Troubleshoot Pod startup issues

Run command `kubectl describe pods`. This will show some error messages thrown during startup.

## Persistence volumes with Minikube

* Keep in mind that minikube itself is docker container. So when we add persistence volumes to pods those are actually
created inside minikube, not in your local machine. If you need to map those to your local machine then you first need to
give a volume mapping to minikube. 
If minikube is already running you have stop it and delete it first, and then start with mount path.

* Delete minikube
```
minikube delete
```

* Start minikube with mount path

```
minikube start --mount --mount-string="<Path in the Local machice>:<Path inside Minikube>"

Eg: minikube start --mount --mount-string="/hms/data/minikube-data:/hms"
```

## Stateful Set

StatefulSet needs a Headless Service. In normal service traffic is routed to any of the pods under the service
In a StatefulSet traffic can't be routed to any pod randomly, it should be routed to the master node.
Eg: MySql Cluster

### Exposing individual pod in a StatefulSet

* StatefulSet Pods have the `label: statefulset.kubernetes.io/pod-name` which contains their generated name (`<StatefulSet Name>-<Ordinal>`). 
* You can create individual Services for each instance that use that label as their selector to expose the individual instances of the StatefulSet.

```
apiVersion: v1
kind: Service
metadata:
  name: web-0
spec:
  type: LoadBalancer
  externalTrafficPolicy: Local
  selector:
    statefulset.kubernetes.io/pod-name: web-2
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
      nodePort: 30088
```
Refer: https://itnext.io/exposing-statefulsets-in-kubernetes-698730fb92a1

## Minikube Storage Class

* Use `minikube ssh` to access inside minikube machine
* Persistence volumes are normally stored at `/tmp/hostpath-provisioner/default`

## Minikube storage
https://platform9.com/blog/tutorial-dynamic-provisioning-of-persistent-storage-in-kubernetes-with-minikube/

## Ingress

https://kubernetes.io/docs/tasks/access-application-cluster/ingress-minikube/
* First enable ingress addon in minikube using `minikube addons enable ingress`

* Ingress configuration
```
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: example-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /$1
spec:
  rules:
    - host: hello-world.info
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: web-0
                port:
                  number: 80
```

* After apply ingress config using `kubectl apply -f <YML>` use `kubectl get ingress` to see the status of ingress.
```
NAME              CLASS   HOSTS              ADDRESS        PORTS   AGE
example-ingress   nginx   hello-world.info   192.168.49.2   80      12m
```
* Wait till IP appear under `ADDRESS`. Once that is available you can add the DNS entry to your `/etc/hosts` file
```
192.168.49.2 hello-world.info
```
* Then from browser access URL `http://hello-world.info`, you will see the exposed service.

### Adding second path

* Update ingress config as bellow. Second path can be added as 
```
          - path: /v2
            pathType: Prefix
            backend:
              service:
                name: web-1
                port:
                  number: 80
```
* Update ingress config
```
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: example-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /$1
spec:
  rules:
    - host: hello-world.info
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: web-0
                port:
                  number: 80
          - path: /v2
            pathType: Prefix
            backend:
              service:
                name: web-1
                port:
                  number: 80
```
* Second service can be accessed via `http://hello-world.info/v2`

