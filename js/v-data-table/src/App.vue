<template>
  <v-app>
    <v-app-bar color="primary" dark>
      <v-app-bar-title>Vue Data Table</v-app-bar-title>
      <v-spacer></v-spacer>
      <v-switch
          v-model="isDark"
          hide-details
          inset
          :label="isDark ? 'Dark' : 'Light'"
          class="ma-2"
      ></v-switch>
    </v-app-bar>
    <v-main>
      <DataTable />
    </v-main>
  </v-app>
</template>

<script>
import { ref, watch } from 'vue'
import { useTheme } from 'vuetify'
import DataTable from './components/DataTable.vue'

export default {
  name: 'App',
  components: { DataTable },
  setup() {
    const theme = useTheme()
    const isDark = ref(false)

    watch(isDark, (newValue) => {
      theme.global.name.value = newValue ? 'dark' : 'light'
    })

    return {
      isDark,
    }
  },
}
</script>
