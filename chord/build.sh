docker rm -f $(docker ps -a -q -f ancestor=chord-20:client) > /dev/null 2>&1
docker rm -f $(docker ps -a -q -f ancestor=chord-20:anchor) > /dev/null 2>&1
docker network rm $(docker network ls -q -f name=chord-20-net) > /dev/null 2>&1
docker rmi -f $(docker images -q chord-20) > /dev/null 2>&1
python3 buildChord.py

