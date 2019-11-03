docker rm -f $(docker ps -a -q -f ancestor=chord-20:client)
docker rm -f $(docker ps -a -q -f ancestor=chord-20:anchor)
docker network rm -f $(docker network ls -q -f name=chord-20-net)
docker rmi -f $(docker images -q chord-20)
python3 buildChord.py

