echo 'Running HTML examples...'

lein run -m swagger-gen.examples.html.generator

echo 'Running Scala examples...'

lein run -m swagger-gen.examples.scala.generator
