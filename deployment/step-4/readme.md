# Setting up ConfigMaps

* Here we are configuration config map which we will be injecting as ENV variables to the POD.

```yaml

kind: ConfigMap
apiVersion: v1
metadata:
  name: backend-app-1-configs
data:
  TASK_SERVICE_URL: backend-service-2
  DB_HOST: mysql-service
  DB_POST: 3306

```

* In the deployment config we can put the following under container configuration.

```yaml

envFrom:
- configMapRef:
    name: backend-app-1-configs #This should match the name of the config-map

```

* For a full Deployment Configuration refer `backend-service1-depolyment.yml`

