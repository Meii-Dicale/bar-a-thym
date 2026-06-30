import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'barathym',
    themes: {
      barathym: {
        dark: false,
        colors: {
          background:  '#F9F5EE',
          surface:     '#FFFFFF',
          primary:     '#7A9078',
          secondary:   '#C98E5F',
          error:       '#B00020',
          'on-primary': '#FFFFFF',
          'sidebar-bg': '#F3EFE7'
        }
      }
    }
  },
  defaults: {
    VBtn: { rounded: 'lg' },
    VCard: { rounded: 'lg' }
  }
})
