/**
 *  Copyright 2017 SmartThings
 *
 *  Provides a virtual switch.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition (name: "Virtual Switch", namespace: "smartthings", author: "SmartThings", runLocally: true, minHubCoreVersion: '000.021.00001', executeCommandsLocally: true, mnmn: "SmartThings", vid: "generic-switch") {
        capability "Actuator"
        capability "Sensor"
        capability "Switch"
    }

    preferences {}

    tiles(scale: 2) {
        multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.Home.home30", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.Home.home30", backgroundColor:"#FFFFFF", nextState:"turningOn", defaultState: true
                attributeState "turningOn", label:'Turning On', action:"switch.off", icon:"st.Home.home30", backgroundColor:"#00A0DC", nextState:"turningOn"
                attributeState "turningOff", label:'Turning Off', action:"switch.on", icon:"st.Home.home30", backgroundColor:"#FFFFFF", nextState:"turningOff"
            }
        }

        standardTile("explicitOn", "device.switch", width: 2, height: 2, decoration: "flat") {
            state "default", label: "On", action: "switch.on", icon: "st.Home.home30", backgroundColor: "#ffffff"
        }
        standardTile("explicitOff", "device.switch", width: 2, height: 2, decoration: "flat") {
            state "default", label: "Off", action: "switch.off", icon: "st.Home.home30", backgroundColor: "#ffffff"
        }

        main(["switch"])
        details(["switch", "explicitOn", "explicitOff"])

    }
}

	
/*  UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon: "st.contact.contact.closed", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon: "st.contact.contact.open", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}        
		main "button"
		details(["button", "refresh"])
	} */

def parse(String description) {
    log.debug "parse description: $description"

    def attrName = null
    def attrValue = null

    if (description?.startsWith("on/off:")) {
        log.debug "switch command"
        attrName = "switch"
        attrValue = description?.endsWith("1") ? "on" : "off"
    }

    def result = createEvent(name: attrName, value: attrValue)

    log.debug "Parse returned ${result?.descriptionText}"
    return result
}

def on() {
    sendEvent(name: "switch", value: "on", isStateChange: true)
}

def off() {
    sendEvent(name: "switch", value: "off", isStateChange: true)
}


def setStatus(type, status) {
    log.debug("Setting status ${type}: ${status}")
    
    // special handling for non-standard attributes
    if (type == "level") {
        setLevel(Float.parseFloat(status));
    }
    else {
        sendEvent(name: type, value: status)
    }
}

def installed() {
    on()
}
