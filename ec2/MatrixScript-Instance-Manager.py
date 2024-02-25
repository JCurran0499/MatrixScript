import boto3
import os
import json
import time

ec2 = boto3.client('ec2', region_name='us-east-1')
route53 = boto3.client('route53', region_name='us-east-1')
ssm = boto3.client('ssm', region_name='us-east-1')

def lambda_handler(event, context):
    instance_id = os.environ.get('INSTANCE_ID')
    domain = os.environ.get('DOMAIN')
    hosted_zone_id = os.environ.get('HOSTED_ZONE_ID')

    if event['status'] == "start":
        start(instance_id, domain, hosted_zone_id) 

    elif event['status'] == "stop":
        stop(instance_id)

    event['success'] = True
    return {
        'statusCode': 200,
        'body': json.dumps(event)
    }

def start(instance_id, domain, hosted_zone_id):
    ec2.start_instances(InstanceIds=[instance_id])

    has_ip = False
    while not has_ip:
        try:
            resp = ec2.describe_instances(InstanceIds=[instance_id])
            inst = resp['Reservations'][0]['Instances'][0]
            ip = inst['PublicIpAddress']
            
            has_ip = True
        except:
            time.sleep(1)

    route53.change_resource_record_sets(
        HostedZoneId=hosted_zone_id,
        ChangeBatch={
            'Changes': [
                {
                    'Action': 'UPSERT',
                    'ResourceRecordSet': {
                        'Name': domain,
                        'Type': 'A',
                        'ResourceRecords': [
                            {
                                'Value': ip
                            }
                        ],
                        'TTL': 300
                    }
                }
            ]
        }
    )

    is_ssm_ready = False
    while not is_ssm_ready:
        try:
            ssm.send_command(
                DocumentName='AWS-RunShellScript',
                InstanceIds=[instance_id],
                Parameters={'commands': [
                    'cd /home/ec2-user/MatrixScript-Backend',
                    'java -jar target/MatrixScript-1.0-jar-with-dependencies.jar > /dev/null 2> /dev/null < /dev/null &',
                    'echo $! > deploy/pid.txt'
                ]}
            )

            is_ssm_ready = True
        except Exception as e:
            if "botocore.errorfactory.InvalidInstanceId" in str(e.__class__):
                time.sleep(5)
            else:
                raise e


def stop(instance_id):
    ec2.stop_instances(InstanceIds=[instance_id])
