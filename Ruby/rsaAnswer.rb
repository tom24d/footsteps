# encoding: UTF-8


class CryptoFactory
  def initialize(prime_number1,prime_number2)
    #ここでキーの生成を行う
    @p1=prime_number1
    @p2=prime_number2
    @mltprime=@p1*@p2
    @minusone = (@p1-1)*(@p2-1)
    @ee = @minusone+rand(25)+100
    loop do
      @ee+=1
      if MathModule.coprime_pair?(@minusone,@ee) then
        break
      end
    end
    @dd=@ee
    loop do
      @dd+=1
      if (@ee*@dd) % @minusone == 1 then
        break
      end
    end

    @pub=PublicKey.new(@ee,@mltprime)
    @pri=PrivateKey.new(@dd,@mltprime)
    puts "KeyGenerate Successful"
  end

    def getPublicKey
      return @pub
    end

    def getPrivateKey
        return @pri
    end

end

class PublicKey
  def initialize(ee,nn)
    @ee=ee
    @nn=nn
  end

  def get_e
    return @ee
  end

  def get_n
    return @nn
  end
end

class PrivateKey
  def initialize(dd,nn)
    @dd=dd
    @nn=nn
  end

  def get_d
    return @dd
  end

  def get_n
    return @nn
  end
end



module MathModule

    def prime_number?(n)
      #nが素数だったらtrueを返す
      if n < 2
        return false
      elsif n == 2
        return true
      end

      (2..n-1).each do |x|
        return false if n % x == 0
      end

      return true
    end

    def coprime_pair?(a,b)
      #a,bが互いに素だったらtrueを返す
    comp = [a.to_i,b.to_i]
    bigger = comp.max
    smaller = comp.min
    amari = 1

        until amari == 0 do
          amari = bigger%smaller
          bigger = smaller
          smaller = amari

        end

        return false if bigger >= 2
        return true if bigger == 1

    end

    def get_primenumber(n)
      #n番目の素数を返す

      prime=0

      counted = 0
      try = 0

      until counted == n do
      tried = 0
      tried = try if prime_number?(try)
        if tried != 0 then
          prime = tried
          counted += 1
          tried = 0
        end
      try+=1
      end

      return prime

    end

    module_function :get_primenumber
    module_function :coprime_pair?
    module_function :prime_number?

end



num = rand(120)+35
diff = rand(29)+1
puts "#{num-diff}番目と#{num}番目の素数を求めます."

prime1=MathModule.get_primenumber(num-diff)
prime2=MathModule.get_primenumber(num)

cry = CryptoFactory.new(prime1,prime2)
pub = cry.getPublicKey
pri = cry.getPrivateKey

puts "#{num-diff}番目:#{prime1}"
puts "#{num}番目:#{prime2}"


puts"秘密鍵d : #{pri.get_d}"
puts"公開鍵e : #{pub.get_e}"
chiphered = []
str1 = "He is a nuts.".unpack("C*")
str1.each{|st|
  chi = (st**pub.get_e)%pub.get_n
chiphered.push(chi)
}
puts "平文: He is a nuts."
puts "平文(バイト列): #{str1}"
puts "暗号化された文: #{chiphered}"

dechiphered = []
chiphered.each { |chi|
dechiphered.push((chi**pri.get_d)%pri.get_n)

}
puts "復号化文(バイト列) : #{dechiphered}"
puts "復号化された文 : #{dechiphered.pack("C*")}"
