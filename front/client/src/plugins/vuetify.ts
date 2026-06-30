import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'barathym',
    themes: {
      barathym: {
        dark: false,
        colors: {
          background: '#F9F5EE',
          primary:    '#7A9078',
          secondary:  '#C98E5F',
          surface:    '#FFFFFF',
          error:      '#B00020'
        }
      }
    }
  }
})
