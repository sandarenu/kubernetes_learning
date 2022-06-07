* Install ELK

https://www.bogotobogo.com/DevOps/Docker/Docker_Kubernetes_ElasticSearch_with_Helm_minikube.php

https://logz.io/blog/fluent-bit-tutorial/


$ kubectl create namespace logging
$ kubectl create -f https://raw.githubusercontent.com/fluent/fluent-bit-kubernetes-logging/master/fluent-bit-service-account.yaml
$ kubectl create -f https://raw.githubusercontent.com/fluent/fluent-bit-kubernetes-logging/master/fluent-bit-role-1.22.yaml
$ kubectl create -f https://raw.githubusercontent.com/fluent/fluent-bit-kubernetes-logging/master/fluent-bit-role-binding-1.22.yaml

$ kubectl create -f https://raw.githubusercontent.com/fluent/fluent-bit-kubernetes-logging/master/output/elasticsearch/fluent-bit-configmap.yaml

$ kubectl create -f https://raw.githubusercontent.com/fluent/fluent-bit-kubernetes-logging/master/output/elasticsearch/fluent-bit-ds-minikube.yaml

## Port forwards

* Elastic
`kubectl port-forward svc/elasticsearch-master 9200`
* Kibana
`kubectl port-forward deployment/kibana-kibana 5601`