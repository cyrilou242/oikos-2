# https://quarkus.io/guides/building-native-image 
./mvnw clean install -Dnative -DskipTests -Dquarkus.native.container-build=true
# see https://console.cloud.google.com/apis/api/cloudbuild.googleapis.com/metrics?project=oikos-2-425220
# todo put a better tag with a commit
TAG=gcr.io/oikos-2-425220/oikos
gcloud builds submit --tag ${TAG}
gcloud run deploy oikos-webapp --image ${TAG} \
  --env-vars-file .github/.prod.env.yaml \
  --region europe-west1 \
  --max-instances 1 \
  --allow-unauthenticated

# todo disable health checks


# domain mapping - only needs to be done once
# maybe add --region europe-west1
# gcloud beta run domain-mappings create --service oikos-webapp --domain oikos.catheu.tech
# gcloud beta run domain-mappings describe --domain oikos.catheu.tech
