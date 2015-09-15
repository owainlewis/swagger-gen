echo 'Running HTML examples...'

lein run -m swagger-gen.examples.html.generator

echo 'Running Scala examples...'

lein run -m swagger-gen.examples.scala.generator

echo 'Runing golang examples...'

lein run -m swagger-gen.examples.golang.generator
