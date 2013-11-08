#echo '8M\n\n'
#time ./rdfgears -Xmx8M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx8M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx8M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*

#echo '16M\n\n'
#time ./rdfgears -Xmx16M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx16M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx16M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*

#echo '32M\n\n'
#time ./rdfgears -Xmx32M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx32M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx32M -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*

echo '64M\n\n'
time ./rdfgears -Xmx64M -w tmp2 --cacheType=mix --diskBased > /dev/null
rm bdb/*
time ./rdfgears -Xmx64M -w tmp2 --cacheType=mix --diskBased > /dev/null
rm bdb/*
time ./rdfgears -Xmx64M -w tmp2 --cacheType=mix --diskBased > /dev/null
rm bdb/*

#echo '1G\n\n'
#time ./rdfgears -Xmx1G -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx1G -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
#time ./rdfgears -Xmx1G -w tmp2 --cacheType=mix --diskBased > /dev/null
#rm bdb/*
