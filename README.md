# Architecture

### Services

**AuthUserMicroService** manage auth and users operations login, register, update profile, update password ...
<br/>
**InterviewMicroService** manage appointments requests, updates, cancels ...
<br/>
**NotificationMicroService** manage sending SMS via Twilio and Email via Firbase and track notifications history ...
<br/>
**Registry & Gateway** edge services for better communication and routing ...
<br/>
### Kafka 
make sure to have Kafka up and running on port [http://localhost:9092](http://localhost:9092)
follow this instructions :
1. Start Zookeeper : 
<code>bin/zookeeper-server-start.sh config/zookeeper.properties</code>
2. Start Kafka : 
<code>bin/kafka-server-start.sh config/server.properties</code>
3. Verify the content of topic : 
<code>bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092</code>
