import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL71fpOfVR2EvH0IanSP8cGXYZCRbYlc\n' +
  'vl7JOcz4zA/8OtqlM0AJIUp5CnjpuPezXpQA1WN/zhexIFLXKaSkzokCAwEAAQ=='

const privateKey = 'MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAvvV+k59VHYS8fQhq\n' +
  'dI/xwZdhkJFtiVy+Xsk5zPjMD/w62qUzQAkhSnkKeOm497NelADVY3/OF7EgUtcp\n' +
  'pKTOiQIDAQABAkEAuYxRMZ/gVI5lmBhVWG2Fu1YctxyYEqDLWc6tGz2lCuGbIY5J\n' +
  'QwqDhA5WPISLZYveAV5VUrNZJ0U1StU13GgTAQIhAPZBpWL3W+RQuHcm/V4h1Dmg\n' +
  '6GPefvSQ7sgvOImtQxBrAiEAxoO8O4CvhwP40MSQuqJSIxhqrxI3/XCCrgIhUtaW\n' +
  'CdsCIHg3z/+idm/doFA67pIXvb1sekbXBe9rGccMlnNP7F+FAiBhIqzEYN+CrUre\n' +
  'vJAlArA2RAhPyhZjiN4ozxoHMsEP6QIhAODZ2vZ15g3HYy+eFDZAZp11UZmzOrsf\n' +
  'IzoH9i3Zwtcs'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

