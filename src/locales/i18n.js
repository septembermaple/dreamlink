import { ref, computed, watch } from 'vue'
import zh from './zh'
import en from './en'

// 支持的语言列表
const supportedLocales = ['zh', 'en']

// 从本地存储获取语言设置，默认中文
const getDefaultLocale = () => {
  const savedLocale = localStorage.getItem('locale')
  if (savedLocale && supportedLocales.includes(savedLocale)) {
    return savedLocale
  }
  // 尝试从浏览器语言设置获取
  const browserLocale = navigator.language.split('-')[0]
  if (supportedLocales.includes(browserLocale)) {
    return browserLocale
  }
  return 'zh'
}

// 当前语言
const currentLocale = ref(getDefaultLocale())

// 语言包
const messages = {
  zh,
  en
}

// 翻译函数
const t = (key, replace = {}) => {
  const keys = key.split('.')
  let value = messages[currentLocale.value]
  
  for (const k of keys) {
    if (value && typeof value === 'object' && k in value) {
      value = value[k]
    } else {
      return key // 如果找不到翻译，返回原键
    }
  }
  
  // 替换变量
  if (typeof value === 'string' && Object.keys(replace).length > 0) {
    return Object.entries(replace).reduce((result, [placeholder, replacement]) => {
      return result.replace(new RegExp(`\\{${placeholder}\\}`), replacement)
    }, value)
  }
  
  return value
}

// 切换语言
const setLocale = (locale) => {
  if (supportedLocales.includes(locale)) {
    currentLocale.value = locale
    localStorage.setItem('locale', locale)
  }
}

// 监听语言变化
watch(currentLocale, (newLocale) => {
  document.documentElement.lang = newLocale
})

// 初始化设置文档语言
document.documentElement.lang = currentLocale.value

export {
  currentLocale,
  supportedLocales,
  t,
  setLocale
}
