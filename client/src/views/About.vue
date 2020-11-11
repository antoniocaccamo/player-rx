<template>
  <div class="about">
    <b-breadcrumb>
      <b-breadcrumb-item href="#home">
        <b-icon
          icon="house-fill"
          scale="1.25"
          shift-v="1.25"
          aria-hidden="true"
        ></b-icon>
        Home
      </b-breadcrumb-item>
      <b-breadcrumb-item href="#foo">Foo</b-breadcrumb-item>
      <b-breadcrumb-item href="#bar">Bar</b-breadcrumb-item>
      <b-breadcrumb-item active>Baz</b-breadcrumb-item>
    </b-breadcrumb>

    <h1>This is an about page</h1>

    <b-alert show>Default Alert</b-alert>

    <b-alert variant="success" show>Success Alert</b-alert>

    <b-alert v-model="showDismissibleAlert" variant="danger" dismissible>
      Dismissible Alert!
    </b-alert>

    <b-alert
      :show="dismissCountDown"
      dismissible
      variant="warning"
      @dismissed="dismissCountDown = 0"
      @dismiss-count-down="countDownChanged"
    >
      <p>This alert will dismiss after {{ dismissCountDown }} seconds...</p>
      <b-progress
        variant="warning"
        :max="dismissSecs"
        :value="dismissCountDown"
        height="4px"
      ></b-progress>
    </b-alert>

    <b-button @click="showAlert" variant="info" class="m-1">
      Show alert with count-down timer
    </b-button>
    <b-button @click="showDismissibleAlert = true" variant="info" class="m-1">
      Show dismissible alert ({{ showDismissibleAlert ? "visible" : "hidden" }})
    </b-button>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { BAlert, BButton } from "bootstrap-vue";

@Component({
  components: {
    BAlert,
    BButton,
  },
})
export default class About extends Vue {
  private dismissSecs = 10;
  private dismissCountDown = 0;
  private showDismissibleAlert = false;
  private submitted = false;

  countDownChanged(dismissCountDown: number) {
    this.dismissCountDown = dismissCountDown;
  }

  showAlert() {
    this.dismissCountDown = this.dismissSecs;
  }
}
</script>
