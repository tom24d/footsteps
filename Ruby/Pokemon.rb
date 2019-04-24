# encoding: UTF-8

class Monster

  def initialize(name,index,hp,attack)

    @index = index
    @name = name
    @hp = hp
    @attack = attack

  end


  def fightWith(monster)
    if @attack<monster.attack then


    elsif @attack>monster.attack then

    else

    end


  end

  attr_accessor :hp
  attr_accessor :attack

end

r = Random.new
zeni = Monster.new("ゼニガメ",0,r.rand(30..45),r.rand(5..10))
pika = Monster.new("ピカチュウ",1,r.rand(30..45),r.rand(5..10))
index =0

if zeni.attack<pika.attack then
index=1
elsif zeni.attack>pika.attack then
index=0
else
  puts "引き分けです."
  exit!
end

num =1

puts "***************"
puts "戦闘開始"
puts "***************"

until zeni.hp<=0||pika.hp<=0 do

  puts "--------------"
  puts "第"+num.to_s+"戦"
  puts "--------------"

  if(index == 0)
    pika.hp-=zeni.attack
  else
    zeni.hp-=pika.attack
  end

  puts "ピカチュウの残りHP: "+pika.hp.to_s
  puts "ゼニガメの残りHP: "+zeni.hp.to_s

sleep(1)

num+=1

end

sleep(2)
puts "***************"
puts "決着！！"
puts "***************"


if(index == 0)
  puts "ピカチュウ瀕死、、ゼニガメの勝利！！"
else
  puts "ピカチュウ瀕死、、ゼニガメの勝利！！"
end
